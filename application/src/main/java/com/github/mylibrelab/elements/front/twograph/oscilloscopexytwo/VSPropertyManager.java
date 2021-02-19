/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.mylibrelab.elements.front.twograph.oscilloscopexytwo;/*
                                                                         * Copyright (C) 2020 MyLibreLab
                                                                         * Based on MyOpenLab by Carmelo Salafia
                                                                         * www.myopenlab.de
                                                                         * Copyright (C) 2004 Carmelo Salafia
                                                                         * cswi@gmx.de
                                                                         *
                                                                         * This program is free software: you can
                                                                         * redistribute it and/or modify
                                                                         * it under the terms of the GNU General Public
                                                                         * License as published by
                                                                         * the Free Software Foundation, either version
                                                                         * 3 of the License, or
                                                                         * (at your option) any later version.
                                                                         *
                                                                         * This program is distributed in the hope that
                                                                         * it will be useful,
                                                                         * but WITHOUT ANY WARRANTY; without even the
                                                                         * implied warranty of
                                                                         * MERCHANTABILITY or FITNESS FOR A PARTICULAR
                                                                         * PURPOSE. See the
                                                                         * GNU General Public License for more details.
                                                                         *
                                                                         * You should have received a copy of the GNU
                                                                         * General Public License
                                                                         * along with this program. If not, see
                                                                         * <http://www.gnu.org/licenses/>.
                                                                         *
                                                                         */

import java.lang.reflect.InvocationTargetException;

import VisualLogic.variables.*;

public class VSPropertyManager {
    private Properties properties;
    private Object classRef;


    public VSPropertyManager(Properties properties, Object classRef) {
        this.properties = properties;
        this.classRef = classRef;

        GenerateVSObjects();
    }



    public Object getMethodReturnValue(String propertyName) {
        Method method;
        String returnType;
        Object returnValue = null;
        try {
            method = classRef.getClass().getMethod("get" + propertyName);

            returnValue = method.invoke(classRef, (Object[]) null);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

        return returnValue;
    }


    public Class getMethodReturnType(String propertyName) {
        Method method;
        try {
            method = classRef.getClass().getMethod("get" + propertyName);

            return method.getReturnType();

        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void setMethodValue(String propertyName, Object param) {
        Method method;

        try {
            Class paramClass = getMethodReturnType(propertyName);
            method = classRef.getClass().getMethod("set" + propertyName, paramClass);
            method.invoke(classRef, param);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

        // System.out.println( p );

    }



    public void GenerateVSObjects() {
        VSObject[] vsproperties = new VSObject[properties.size()];

        Property p;
        int c = 0;
        for (int i = 0; i < properties.size(); i++) {
            p = properties.get(i);

            Class paramType = getMethodReturnType(p.name);

            if (paramType == String.class) {
                p.vsProperty = new VSString((String) getMethodReturnValue(p.name));
            } else if (paramType == Boolean.class) {
                p.vsProperty = new VSBoolean((Boolean) getMethodReturnValue(p.name));
            } else if (paramType == Integer.class) {
                p.vsProperty = new VSInteger((Integer) getMethodReturnValue(p.name));
            } else if (paramType == Double.class) {
                p.vsProperty = new VSDouble((Double) getMethodReturnValue(p.name));
            } else if (paramType == Font.class) {
                p.vsProperty = new VSFont((Font) getMethodReturnValue(p.name));
            } else if (paramType == Color.class) {
                p.vsProperty = new VSColor((Color) getMethodReturnValue(p.name));
            }


        }

    }



}
