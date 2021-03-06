package main;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.*;

public class CommandManager {
	Calculator calc = new Calculator();
	ExpressionEval eval = new ExpressionEval();
	GraphWindow graphWin = new GraphWindow(calc);
	List<String> args;

	/**
	 * takes in a string and extracts the command and arguments. Then calls and
	 * returns the data in the form of a string
	 * 
	 * @param input
	 * @return
	 */
	public void ParseInput(String input) {
		input += " ";
		String command = input.substring(0, input.indexOf(" "));
		args = getArgs(input);

		if (Main.debug) {
			System.out.println("Args with received: " + command + " :command:");
			for (String arg : args) {
				System.out.print(arg + ", ");
			}
			System.out.println();
		}

		try {
			switch (command) {
			case ("help"):
				System.out.println(Main.helpText);
				break;
			case ("setFunction"):
				setFunction();
				break;
			case ("sf"):
				setFunction();
				break;
			case ("sfc"):
				setFunctionColor(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)),
						Integer.parseInt(args.get(2)), Integer.parseInt(args.get(3)));
				break;
			case ("sft"):
				setFunctionType();
				break;
			case ("zero"):
				System.out.println("x= " + calc.zero(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1))));
				break;
			case ("cf"):
				if (args.size() == 0) {
					calc.clearFunc();
				} else {
					calc.clearFunc(Integer.parseInt(args.get(0)));
				}
				break;
			case ("view"):
				calc.displayFunctions();
				break;
			case ("clear"):
				for (int i = 0; i < 50; i++) {
					System.out.println();
				}
				break;
			case ("debug"):
				Main.debug = Boolean.parseBoolean(args.get(0));
				break;
			case ("graph"):
				if (args.size() > 0) {
					setGraphBounds();
				}
				Graph();
				break;
			case ("eval"):
				System.out.println(calc.InterpretFunc(Integer.parseInt(args.get(0)), Double.parseDouble(args.get(1))));
				break;
			case ("exit"):
				Main.running = false;
				break;
			default:
				System.out.println(eval.Evaluate(input));
			}
		} catch (Exception e) {
			if (Main.debug)
				e.printStackTrace();
			System.out.println("syntax error. Invalid arguments");
		}
	}

	private void setGraphBounds() {
		graphWin.graph.setGraphBounds(Double.parseDouble(args.get(0)), Double.parseDouble(args.get(1)),
				Double.parseDouble(args.get(2)), Double.parseDouble(args.get(3)));
	}

	private void Graph() {
		graphWin.graph();
	}

	private void setFunction() {
		if (args.size() == 1) {
			calc.setFunction(args.get(0), calc.getNextEmptyFunc(), FunctionData.FunctionType.normal);
		} else {
			switch (args.get(2)) {
			case "0":
				calc.setFunction(args.get(0), Integer.parseInt(args.get(1)), FunctionData.FunctionType.normal);
				break;
			case "1":
				calc.setFunction(args.get(0), Integer.parseInt(args.get(1)), FunctionData.FunctionType.derivative);
				break;
			case "2":
				calc.setFunction(args.get(0), Integer.parseInt(args.get(1)), FunctionData.FunctionType.integral);
				break;
			default:
				System.out.println(
						"not a recognized function type. Use 0-2. 0 is normal, 1 is derivitive, 2 is integral");
			}
		}
	}

	private void setFunctionType() {
		switch (args.get(1)) {
		case "0":
			calc.functions.get(Integer.parseInt(args.get(0))).type = FunctionData.FunctionType.normal;
			break;
		case "1":
			calc.functions.get(Integer.parseInt(args.get(0))).type = FunctionData.FunctionType.derivative;
			break;
		case "2":
			calc.functions.get(Integer.parseInt(args.get(0))).type = FunctionData.FunctionType.integral;
			break;
		default:
			System.out.println("invalid function type.Use 0-2. 0 is normal, 1 is derivitive, 2 is integral");
		}

	}

	private void setFunctionColor(int index, String colorName) {
		Color color;
		try {
			Field field = Class.forName("java.awt.Color").getField("colorName");
			color = (Color) field.get(null);
		} catch (Exception e) {
			System.out.println("invalid color");
			color = null; // Not defined
		}
		calc.functions.get(index).color = color;
	}

	private void setFunctionColor(int index, int r, int g, int b) {
		Color color = new Color(r, g, b);
		calc.functions.get(index).color = color;
	}

	/**
	 * gets all terms enclosed by spaces in input
	 * 
	 * @param _input
	 * @return a list of arguments
	 */
	private List<String> getArgs(String _input) {
		List<String> args = new ArrayList<String>();
		for (int i = 1; getOccurrenceIndex(i + 1, _input, " ") != -1; i++) {
			args.add(_input.substring(getOccurrenceIndex(i, _input, " ") + 1, getOccurrenceIndex(i + 1, _input, " ")));

		}

		return args;
	}

	/**
	 * gets the index of the nth occurrence of a character in a string. Returns -1
	 * if the character is not found
	 * 
	 * @param _occurrence - the occurrence amount
	 * @param _input      - the string
	 * @param _character  - the character to look for
	 * @return
	 */
	public int getOccurrenceIndex(int _occurrence, String _input, String _character) {
		if (_input.contains(_character)) {
			int index = 0;
			int tempIndex = 0;
			// index starts at 0 and moves up the string to each occurrence of character
			for (int i = 0; i < _occurrence; i++) {
				if (!_input.contains(_character)) {
					index = -1;
					break;
				}
				tempIndex = _input.indexOf(_character);
				_input = _input.substring(tempIndex + 1);
				if (i == 0) {
					index += tempIndex;
				} else {
					index += tempIndex + 1;
				}
			}

			return index;
		} else {
			return -1;
		}

	}

}
