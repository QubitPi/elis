/*
 * Copyright Jiaqi Liu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.QubitPi.elis.config;

import com.yahoo.elide.Elide;
import com.yahoo.elide.ElideSettings;
import com.yahoo.elide.ElideSettingsBuilder;
import com.yahoo.elide.core.DataStore;
import com.yahoo.elide.core.EntityDictionary;
import com.yahoo.elide.datastores.jpa.JpaDataStore;
import com.yahoo.elide.datastores.jpa.transaction.NonJtaTransaction;
import com.yahoo.elide.resources.DefaultOpaqueUserFunction;
import com.yahoo.elide.standalone.Util;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.Collections;
import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.SecurityContext;

/**
 * Elis Webservice configuration.
 */
@Immutable
@ThreadSafe
public class ElisResourceConfig extends ResourceConfig {

    /**
     * Constructor.
     */
    @Inject
    public ElisResourceConfig() {
        register(new ElisBinder());
    }

    /**
     * A {@link AbstractBinder binder} that binds resources needed by Elis Webservice.
     */
    private class ElisBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(getUserExtractionFunction()).to(DefaultOpaqueUserFunction.class).named("elideUserExtractionFunction");

            final ElideSettings elideSettings = getElideSettings();

            bind(new Elide(elideSettings)).to(Elide.class).named("elide");

            bind(elideSettings).to(ElideSettings.class);

            bind(elideSettings.getDictionary()).to(EntityDictionary.class);
            bind(elideSettings.getDataStore()).to(DataStore.class).named("elideDataStore");
        }

        /**
         * Returns a new function used to extract a user from the {@link SecurityContext}.
         *
         * @return  Function for user extraction
         */
        private DefaultOpaqueUserFunction getUserExtractionFunction() {
            return SecurityContext::getUserPrincipal;
        }

        /**
         * Returns Elide configuration.
         *
         * @return  Elide settings
         */
        private ElideSettings getElideSettings() {
            final EntityManagerFactory entityManagerFactory = Util.getEntityManagerFactory(
                    "com.github.QubitPi.elis.bean",
                    new Properties()
            );
            final DataStore dataStore = new JpaDataStore(
                    entityManagerFactory::createEntityManager,
                    NonJtaTransaction::new
            );
            final EntityDictionary entityDictionary = new EntityDictionary(Collections.emptyMap());

            return new ElideSettingsBuilder(dataStore)
                    .withEntityDictionary(entityDictionary)
                    .build();
        }

    }
}
