package model;

import components.ChessPiece;

import java.util.Stack;

public class GameStack{
	public static Stack<ChessPiece[][]> oldBoard = new Stack<>();
	public static Stack<ChessPiece> oldPlayer = new Stack<>();


	public static <E> Stack<E> reverseStack(Stack<E> stack) {
		Stack<E> res = new Stack<>();
		Stack<E> copy = (Stack<E>) stack.clone();
 		while (copy.size() > 0) {
			res.push(copy.pop());
		}
		return res;
	}
}
