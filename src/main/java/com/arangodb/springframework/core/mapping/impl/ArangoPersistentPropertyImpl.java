/*
 * DISCLAIMER
 *
 * Copyright 2016 ArangoDB GmbH, Cologne, Germany
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
 *
 * Copyright holder is ArangoDB GmbH, Cologne, Germany
 */

package com.arangodb.springframework.core.mapping.impl;

import java.util.Optional;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.util.StringUtils;

import com.arangodb.springframework.core.mapping.ArangoPersistentProperty;
import com.arangodb.springframework.core.mapping.Field;
import com.arangodb.springframework.core.mapping.Ref;

/**
 * @author Mark - mark at arangodb.com
 *
 */
public class ArangoPersistentPropertyImpl extends AnnotationBasedPersistentProperty<ArangoPersistentProperty>
		implements ArangoPersistentProperty {

	private final FieldNamingStrategy fieldNamingStrategy;

	public ArangoPersistentPropertyImpl(final Property property,
		final PersistentEntity<?, ArangoPersistentProperty> owner, final SimpleTypeHolder simpleTypeHolder,
		final FieldNamingStrategy fieldNamingStrategy) {
		super(property, owner, simpleTypeHolder);
		this.fieldNamingStrategy = fieldNamingStrategy != null ? fieldNamingStrategy
				: PropertyNameFieldNamingStrategy.INSTANCE;
	}

	@Override
	protected Association<ArangoPersistentProperty> createAssociation() {
		return new Association<>(this, null);
	}

	@Override
	public Optional<Ref> getRef() {
		return findAnnotation(Ref.class);
	}

	@Override
	public String getFieldName() {
		return isIdProperty() ? "_id" : getAnnotatedFieldName().orElse(fieldNamingStrategy.getFieldName(this));
	}

	private Optional<String> getAnnotatedFieldName() {
		return findAnnotation(Field.class).map(f -> StringUtils.hasText(f.value()) ? f.value() : null);
	}

}
