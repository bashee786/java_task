package com.example.demo.model;

import java.util.Date;

import lombok.Data;

@Data
public class IssuedBooks {

	private Integer bookId;
	private String name;
	private String price;
	private String author;
	private Date date;
}
