package com.djbabs.cardservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericResponse {

	private boolean success;
	private Object payload;
}
