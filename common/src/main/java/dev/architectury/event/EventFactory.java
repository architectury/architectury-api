/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package dev.architectury.event;

import com.google.common.reflect.AbstractInvocationHandler;
import dev.architectury.annotations.ForgeEvent;
import dev.architectury.annotations.ForgeEventCancellable;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class EventFactory {
    private EventFactory() {
    }
    
    public static <T> Event<T> of(Function<List<T>, T> function) {
        return new EventImpl<>(function);
    }
    
    @SafeVarargs
    public static <T> Event<T> createLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createLoop((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createLoop(Class<T> clazz) {
        return of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (var listener : listeners) {
                    method.invoke(listener, args);
                }
                return null;
            }
        }));
    }
    
    @SafeVarargs
    public static <T> Event<T> createEventResult(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createEventResult((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createEventResult(Class<T> clazz) {
        return of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (EventResult) Objects.requireNonNull(method.invoke(listener, args));
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return EventResult.pass();
            }
        }));
    }
    
    @SafeVarargs
    public static <T> Event<T> createCompoundEventResult(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createCompoundEventResult((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createCompoundEventResult(Class<T> clazz) {
        return of(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (CompoundEventResult) Objects.requireNonNull(method.invoke(listener, args));
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return CompoundEventResult.pass();
            }
        }));
    }
    
    @SafeVarargs
    public static <T> Event<Consumer<T>> createConsumerLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createConsumerLoop((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<Consumer<T>> createConsumerLoop(Class<T> clazz) {
        Event<Consumer<T>> event = of(listeners -> (Consumer<T>) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{Consumer.class}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (var listener : listeners) {
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
    
    @SafeVarargs
    public static <T> Event<EventActor<T>> createEventActorLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createEventActorLoop((Class<T>) typeGetter.getClass().getComponentType());
    }
    
    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<EventActor<T>> createEventActorLoop(Class<T> clazz) {
        Event<EventActor<T>> event = of(listeners -> (EventActor<T>) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{EventActor.class}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(@NotNull Object proxy, @NotNull Method method, Object @NotNull [] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (EventResult) method.invoke(listener, args);
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return EventResult.pass();
            }
        }));
        Class<?> superClass = clazz;
        do {
            
            if (superClass.isAnnotationPresent(ForgeEventCancellable.class)) {
                return attachToForgeEventActorCancellable(event);
            }
            superClass = superClass.getSuperclass();
        } while (superClass != null);
        superClass = clazz;
        do {
            
            if (superClass.isAnnotationPresent(ForgeEvent.class)) {
                return attachToForgeEventActor(event);
            }
            superClass = superClass.getSuperclass();
        } while (superClass != null);
        return event;
    }
    
    @ExpectPlatform
    @ApiStatus.Internal
    public static <T> Event<Consumer<T>> attachToForge(Event<Consumer<T>> event) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Internal
    public static <T> Event<EventActor<T>> attachToForgeEventActor(Event<EventActor<T>> event) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Internal
    public static <T> Event<EventActor<T>> attachToForgeEventActorCancellable(Event<EventActor<T>> event) {
        throw new AssertionError();
    }
    
    private static class EventImpl<T> implements Event<T> {
        private final Function<List<T>, T> function;
        private T invoker = null;
        private ArrayList<T> listeners;
        
        public EventImpl(Function<List<T>, T> function) {
            this.function = function;
            this.listeners = new ArrayList<>();
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
                invoker = function.apply(listeners);
            }
        }
    }
}
