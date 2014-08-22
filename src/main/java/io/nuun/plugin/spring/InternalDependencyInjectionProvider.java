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

import io.nuun.kernel.api.plugin.PluginException;
import io.nuun.kernel.spi.DependencyInjectionProvider;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.google.inject.Module;

class InternalDependencyInjectionProvider implements DependencyInjectionProvider
{
    @Override
    public boolean canHandle(Class<?> injectionDefinition)
    {
        return ConfigurableListableBeanFactory.class.isAssignableFrom(injectionDefinition) || ConfigurableApplicationContext.class.isAssignableFrom(injectionDefinition);
    }

    @Override
    public Module convert(Object injectionDefinition)
    {
        if (injectionDefinition instanceof ConfigurableListableBeanFactory)
            return new SpringModule((ConfigurableListableBeanFactory)injectionDefinition);
        else if (injectionDefinition instanceof ConfigurableApplicationContext)
            return new SpringModule(((ConfigurableApplicationContext)injectionDefinition).getBeanFactory());
        else
            throw new PluginException("Only ConfigurableListableBeanFactory or ConfigurableApplicationContext types are handled");
    }

    @Override
    public Object kernelDIProvider()
    {
        return null;
    }

}