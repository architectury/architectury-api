/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package me.shedaniel.architectury.event;

import com.google.common.reflect.AbstractInvocationHandler;
import me.shedaniel.architectury.ExpectPlatform;
import me.shedaniel.architectury.ForgeEvent;
import net.jodah.typetools.TypeResolver;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class EventFactory {
    private EventFactory() {}
    
    public static <T> Event<T> create(Function<T[], T> function) {
        Class<?>[] arguments = TypeResolver.resolveRawArguments(Function.class, function.getClass());
        return new EventImpl<>(arguments[1], function);
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createLoop(Class<T> clazz) {
        return create(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (T listener : listeners) {
                    method.invoke(listener, args);
                }
                return null;
            }
        }));
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createInteractionResult(Class<T> clazz) {
        return create(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (T listener : listeners) {
                    InteractionResult result = (InteractionResult) method.invoke(listener, args);
                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
        }));
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createInteractionResultHolder(Class<T> clazz) {
        return create(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (T listener : listeners) {
                    InteractionResultHolder result = (InteractionResultHolder) Objects.requireNonNull(method.invoke(listener, args));
                    if (result.getResult() != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResultHolder.pass(null);
            }
        }));
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<Consumer<T>> createConsumerLoop(Class<T> clazz) {
        Event<Consumer<T>> event = create(listeners -> (Consumer<T>) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{Consumer.class}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (Consumer<T> listener : listeners) {
                    method.invoke(listener, args);
                }
                return null;
            }
        }));
        Class<?> superClass = clazz;
        do {
            if (superClass.isAnnotationPresent(ForgeEvent.class)) {
                return attachToForge(event);
            }
            superClass = superClass.getSuperclass();
        } while (superClass != null);
        return event;
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<Actor<T>> createActorLoop(Class<T> clazz) {
        return create(listeners -> (Actor<T>) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{Actor.class}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (Actor<T> listener : listeners) {
                    InteractionResult result = (InteractionResult) method.invoke(listener, args);
                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            }
        }));
    }
    
    @ExpectPlatform
    public static <T> Event<Consumer<T>> attachToForge(Event<Consumer<T>> event) {
        throw new AssertionError();
    }
    
    private static class EventImpl<T> implements Event<T> {
        private final Function<T[], T> function;
        private T invoker = null;
        private ArrayList<T> listeners;
        private Class<?> clazz;
        
        public EventImpl(Class<?> clazz, Function<T[], T> function) {
            this.clazz = Objects.requireNonNull(clazz);
            this.function = function;
            this.listeners = new ArrayList<>();
        }
        
        private T[] emptyArray() {
            try {
                return (T[]) Array.newInstance(clazz, 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public T invoker() {
            if (invoker == null) {
                update();
            }
            return invoker;
        }
        
        @Override
        public void register(T listener) {
            listeners.add(listener);
            invoker = null;
        }
        
        @Override
        public void unregister(T listener) {
            listeners.remove(listener);
            listeners.trimToSize();
            invoker = null;
        }
        
        @Override
        public boolean isRegistered(T listener) {
            return listeners.contains(listener);
        }
        
        @Override
        public void clearListeners() {
            listeners.clear();
            listeners.trimToSize();
            invoker = null;
        }
        
        public void update() {
            if (listeners.size() == 1) {
                invoker = listeners.get(0);
            } else {
                invoker = function.apply(listeners.toArray(emptyArray()));
            }
        }
    }
}
