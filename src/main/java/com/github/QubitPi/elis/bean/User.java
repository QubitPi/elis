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
package com.github.QubitPi.elis.bean;

import com.yahoo.elide.annotation.Include;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.jcip.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * An user domain model.
 */
@Entity
@Immutable
@Include(rootLevel = true)
public class User {

    /**
     * Surrogate key of this entity.
     */
    @JsonIgnore
    private Long id;

    /**
     * Name of an user.
     */
    private String name;

    /**
     * Returns the primary key of a user.
     *
     * @return surrogate key of a user row
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Sets the primary key of a user.
     *
     * @param id  The new surrogate key for a user row
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the name of this user.
     *
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this user.
     *
     * @param name  The new user name
     */
    public void setName(final String name) {
        this.name = name;
    }
}
