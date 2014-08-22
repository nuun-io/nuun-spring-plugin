/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.nuun.plugin.spring;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.BeanFactory;

import com.google.inject.Provider;

class ByNameSpringContextProvider<T> implements Provider<T>
{
    final BeanFactory beanFactory;
    final Class<T>    type;
    final String      name;

    public ByNameSpringContextProvider(Class<T> type, String name, BeanFactory beanFactory)
    {
        this.type = checkNotNull(type, "type");
        this.name = checkNotNull(name, "name");
        this.beanFactory = beanFactory;
    }

    public T get()
    {
        return type.cast(beanFactory.getBean(name));
    }
}