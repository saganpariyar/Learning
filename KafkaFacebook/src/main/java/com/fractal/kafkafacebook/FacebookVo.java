/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fractal.kafkafacebook;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author karan.gusani
 */
public class FacebookVo {

    private @NonNull
    @Getter
    @Setter
    String key;
    private @NonNull
    @Getter
    @Setter
    String value;
}
