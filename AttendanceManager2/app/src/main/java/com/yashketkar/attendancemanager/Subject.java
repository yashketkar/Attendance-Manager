package com.yashketkar.attendancemanager;

public class Subject {
	 String n;
	 int a;
	 int t;

	 public String getName() {
	  return n;
	 }

	 public void setName(String n) {
	  this.n = n;
	 }

	 public int getAttended() {
	  return a;
	 }

	 public void setAttended(int a) {
	  this.a = a;
	 }

	 public int getTotal() {
	  return t;
	 }

	 public void setTotal(int t) {
	  this.t =t;
	 }

	 public Subject(String n, int a, int t) {
	  super();
	  this.n = n;
	  this.a = a;
	  this.t = t;
	 }

	}