package com.decentralbank.decentralj.core;

import java.util.HashMap;
import java.util.Scanner;

public class CommandLine {	
	
	static HashMap<String, ICommand> map = new HashMap<>();
	
	public static void main(String args[]) {
		map.put("hello", new printHello());
		map.put("string", new printString());
		map.put("add", new add());
		map.get("hello").execute();
		map.get("string").execute("i am here");
		map.get("add").execute(1,2);
	}
}

interface ICommand {
	public void execute(Object ... args);
}

class printHello implements ICommand
{
	@Override
	public void execute(Object ... args) {
		System.out.println("Hello");
	}	
}

class printString implements ICommand
{
	@Override
	public void execute(Object... args) {
		String out = "";
		for(Object s : args)
			out += s;
		System.out.println(out);
	}
}

class add implements ICommand
{

	@Override
	public void execute(Object... args) {
		try {
			if(args.length == 2)
			{
				int a = (int)args[0];
				int b = (int)args[1];
				System.out.println(a + b);
			}
			else throw new Exception("incorrect num of inputs");
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
}
