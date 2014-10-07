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

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;
import static org.fest.assertions.Assertions.assertThat;
import io.nuun.kernel.api.Kernel;
import io.nuun.plugin.spring.sample.AbstractService2;
import io.nuun.plugin.spring.sample.Service1;
import io.nuun.plugin.spring.sample.Service1Internal;
import io.nuun.plugin.spring.sample.Service1InternalBis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class SpringPluginTest {

    static Kernel kernel;
    static Injector injector;


    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void setup() {
        
        kernel = createKernel(
                //
                newKernelConfiguration() //
                .withoutSpiPluginsLoader()
                .plugins(UsingSpringAsDIPlugin.class, SpringPlugin.class)
                
                

                );

        kernel.init();
        kernel.start();

        // add a second guice-only implementation of Service1
        injector = kernel.getObjectGraph().as(Injector.class).createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Service1.class).to(Service1InternalBis.class);
            }
        });
    }

    @AfterClass
    public static void teardown() {
        kernel.stop();
    }

    @Test
    public void spring_bean_should_be_injectable_by_type_and_name() {
        Service1 service1_by_name = injector.getInstance(Key.get(Service1Internal.class, Names.named("service1")));
        assertThat(service1_by_name).isNotNull();
    }

    @Test
    public void spring_bean_should_be_injectable_by_interface_and_name() {
        Service1 service1 = injector.getInstance(Key.get(Service1.class, Names.named("service1")));
        assertThat(service1).isNotNull();
    }

    @Test
    public void spring_bean_should_be_injectable_by_parent_type_and_name() {
        AbstractService2 service2 = injector.getInstance(Key.get(AbstractService2.class, Names.named("service2")));
        assertThat(service2).isNotNull();
    }

    @Test
    public void canHandle_should_return_true_for_application_context_classes() {
        InternalDependencyInjectionProvider provider = new InternalDependencyInjectionProvider();
        ApplicationContext application = new StaticApplicationContext();
        assertThat(provider.canHandle(application.getClass())).isTrue();
    }

    @Test
    public void canHandle_should_return_true_for_bean_factory_classes() {
        InternalDependencyInjectionProvider provider = new InternalDependencyInjectionProvider();
        ApplicationContext application = new StaticApplicationContext();
        assertThat(provider.canHandle(application.getAutowireCapableBeanFactory().getClass())).isTrue();
    }

    @AfterClass
    public static void clear() {
        kernel.stop();
    }
}
