/*
 * Copyright (c) 2020 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {ApplicationComponent} from './application.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ApplicationPageService} from './application.service';

describe('ApplicationComponent', () => {
  let component: ApplicationComponent;
  let fixture: ComponentFixture<ApplicationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApplicationComponent ],
      providers: [
        {provide: ApplicationPageService, useValue: {
            initUrlBindingStreams: () => {},
            unSubscribe: () => {},
          }}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
