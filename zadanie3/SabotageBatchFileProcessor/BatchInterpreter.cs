using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SabotageBatchFileProcessor
{
    public class BatchInterpreter
    {
        private string fileName;
        private List<String> lines;
        private Dictionary<String, Variable> Variables;
        private StringParser SParser;
        public static string[] KEYWORDS = { "int", "string", "print", "cast" };
        public static char[] KEYSIGNS = { '(', ')', '+', '-', '*', '=' };

        public BatchInterpreter(string fileName)
        {
            this.fileName = fileName;
            this.Variables = new Dictionary<string, Variable>();
            this.SParser = new StringParser(this);
        }

        public void process()
        {
            this.lines = FileOp.loadCodeLinesFromFile(this.fileName);
            foreach (String line in lines)
            {
                processCommand(line);
            }
            Console.WriteLine("_________________________________"); //TODO: delete \/
            foreach (KeyValuePair<String, Variable> entry in this.Variables)
            {
                if (entry.Value.VariableType == VariableTypes.INT)
                {
                    Console.WriteLine("i [" + entry.Key + ", " + entry.Value.IValue + "]");
                }
                else
                {
                    Console.WriteLine("s [" + entry.Key + ", " + entry.Value.SValue + "]");
                }
            }
        }

        private void processCommand(String line)
        {
            Console.ForegroundColor = ConsoleColor.DarkGreen;
            Console.WriteLine(line);
            Console.ResetColor();
            Boolean isInt;
            if ((isInt = line.StartsWith("int ")) || line.StartsWith("string ")) // declaration only
            {
                declareVar(line, isInt);
                if (isInt)
                {
                    line = line.Substring(4);
                }
                else
                {
                    line = line.Substring(6);
                }
            }
            if (line.Trim().StartsWith("print")) // print only
            {
                prepareToPrint(line);
            }
            if (SParser.containsKeySigns(line, false)) // operation with assignment
            {
                line = calculate(line);
            }
            if (line.Contains('=')) // assignment
            {
                if (SParser.ContainsKeyWord(line, KEYWORDS[3])) // cast
                {
                    line = prepareAndDoCast(line);
                }
                SParser.assignValue(line);
            }
        }

        private void declareVar(String line, Boolean isInt)
        {
            int iR = line.IndexOf('=');
            int iL = line.IndexOf(';');
            int iVNL;
            Variable variable;
            if (isInt)
            {
                iVNL = 3;
                variable = new Variable(0);
            }
            else
            {
                iVNL = 6;
                variable = new Variable("");
            }
            if (iR == -1)
            {
                iR = iL;
            }
            string varName = line.Substring(iVNL, iR - iVNL).Trim();
            Variables.Add(varName, variable);
        }

        public void assignValue(string varName, string value)
        {
            if (!Variables.ContainsKey(varName))
            {
                throw new Exception("Variable '" + varName + "' not declared!");
            }
            Variables[varName].SValue = value;
        }

        public void assignValue(string varName, int value)
        {
            if (!Variables.ContainsKey(varName))
            {
                throw new NullReferenceException("Variable '" + varName + "' not declared!");
            }
            Variables[varName].IValue = value;
        }

        private string calculate(string line)
        {
            Dictionary<Int32, Char> signIndexes = SParser.getSignIndexes(line);
            foreach (KeyValuePair<Int32, Char> entry in signIndexes) //TODO: delete
            {
                Console.ForegroundColor = ConsoleColor.Blue;
                Console.WriteLine(entry);
                Console.ResetColor();
            }
            int iR = signIndexes.First(x => x.Value == KEYSIGNS[5]).Key;
            if (iR == -1)
            {
                throw new InvalidOperationException("Invalid operation in " + line);
            }
            string newLine = line.Substring(iR + 1);
            string varName = line.Substring(0, iR).Trim();
            if (signIndexes.ContainsValue(KEYSIGNS[0])) //if has something in brackets
            {
                int iOB = signIndexes.First(x => x.Value == KEYSIGNS[0]).Key;
                int iCB = signIndexes.First(x => x.Value == KEYSIGNS[1]).Key;
                string lineIn = line.Substring(iOB, iCB - iOB);
                if (SParser.containsKeySigns(lineIn, false))
                {
                    newLine = newLine.Replace(lineIn, calculate(lineIn));
                }
            }
            else
            {
                Boolean isInt = Variables[varName].VariableType == VariableTypes.INT;
                if (isInt)
                {
                    while (SParser.containsKeySigns(newLine, false))
                    {
                        Dictionary<Int32, Char> signNewIndexes = SParser.getSignIndexes(newLine);
                        if (signNewIndexes.ContainsValue(KEYSIGNS[2]))
                        {
                            newLine = calcIntOp(newLine, signNewIndexes, 2);
                        }
                        else if (signNewIndexes.ContainsValue(KEYSIGNS[3]))
                        {
                            newLine = calcIntOp(newLine, signNewIndexes, 3);
                        }
                        else if (signNewIndexes.ContainsValue(KEYSIGNS[4]))
                        {
                            newLine = calcIntOp(newLine, signNewIndexes, 4);
                        }

                    }
                }
                else
                {

                }
            }
            return varName + "=" + newLine;
        }

        private string calcIntOp(string line, Dictionary<Int32, Char> signIndexes, int signIndex)
        {
            int a = 0;
            int b = 0;
            line = line.Trim();
            int iP = signIndexes.First(x => x.Value == KEYSIGNS[signIndex]).Key;
            int iBiP = getBeforeSignIndex(signIndexes, iP);
            int iAiP = getAfterSignIndex(signIndexes, iP);
            string op = line.Substring(iBiP + 1, iAiP - iBiP - 2);
            string fS = line.Substring(iBiP, iP - iBiP - 1).Trim();
            string sS = line.Substring(iP, iAiP - iP - 1).Trim();
            if (Variables.ContainsKey(fS))
            {
                if (Variables[fS].VariableType == VariableTypes.INT)
                {
                    a = Variables[fS].IValue;
                }
                else
                {
                    throw new Exception("Not casted variable " + fS);
                }
            }
            else if (!int.TryParse(fS, out a))
            {
                throw new ArgumentException("Bad input string '" + fS + "' in " + line);
            }
            if (Variables.ContainsKey(sS))
            {
                if (Variables[fS].VariableType == VariableTypes.INT)
                {
                    b = Variables[sS].IValue;
                }
                else
                {
                    throw new Exception("Not casted variable " + sS);
                }
            }
            else
            {
                if (!int.TryParse(fS, out a))
                {
                    throw new ArgumentException("Bad input string in " + line);
                }
            }
            int res = 0;
            switch (signIndex)
            {
                case 2:
                    res = a + b;
                    break;
                case 3:
                    res = a - b;
                    break;
                case 4:
                    res = a * b;
                    break;
            }
            line = line.Replace(op, res.ToString());
            return line;
        }


        private int getBeforeSignIndex(Dictionary<int, char> dictionary, int index)
        {
            int bI = 0;
            foreach (var entry in dictionary)
            {
                if (entry.Key == index)
                {
                    break;
                }
                bI = entry.Key;
            }
            return bI;
        }

        private int getAfterSignIndex(Dictionary<int, char> dictionary, int index)
        {
            int bI = 0;
            Boolean found = false;
            foreach (var entry in dictionary)
            {
                if (found)
                {
                    return entry.Key;
                }
                else if (entry.Key == index)
                {
                    found = true;
                }
                bI = entry.Key;
            }
            return 0;
        }

        public string prepareAndDoCast(string line)
        {
            Dictionary<Int32, Char> signIndexes = SParser.getSignIndexes(line);
            foreach (KeyValuePair<Int32, Char> entry in signIndexes) //TODO: delete
            {
                Console.ForegroundColor = ConsoleColor.Blue;
                Console.WriteLine(entry);
                Console.ResetColor();
            }
            string newLine = line;

            return "";
        }

        public void prepareToPrint(string line)
        {
            int iFB = line.IndexOf('(');
            int iSB = line.IndexOf(')');
            string vName = line.Substring(iFB + 1, iSB - iFB - 1);
            if (Variables.ContainsKey(vName))
            {
                Variable variable = Variables[vName];
                if (variable.VariableType == VariableTypes.INT)
                {
                    BatchFunctionProcessor.print(variable.IValue);
                }
                else
                {
                    BatchFunctionProcessor.print(variable.SValue);
                }
            }
            else { throw new NullReferenceException("There is no variable " + vName + " declared!"); }
        }
    }
}
