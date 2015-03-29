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
            if (SParser.containsKeySign(line, false)) // operation with assignment
            {
                line = calculate(line);
            }
            if (line.Contains('=')) // assignment only
            {
                SParser.assignValue(line);
            }
        }

        private string calculate(string line)
        {
            string newLine = "";
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
            string varName = line.Substring(0, iR).Trim();
            if (Variables.ContainsKey(varName))
            {
                if (signIndexes.ContainsValue(KEYSIGNS[0]))
                {
                    int iOB = signIndexes.First(x => x.Value == KEYSIGNS[0]).Key;
                    int iCB = signIndexes.First(x => x.Value == KEYSIGNS[1]).Key;
                    string lineIn = line.Substring(iOB, iCB - iOB);
                    if (SParser.containsKeySign(lineIn, false))
                    {
                        string s = calculate(lineIn);
                        newLine.Replace(lineIn, s);
                    }
                }
            }
            newLine = line.Substring(iR);
            return varName + "=" + newLine;
        }

        private bool containsKeyWords(string line)
        {
            for (int i = 0; i < KEYWORDS.Length; i++)
            {
                string keyWord = KEYWORDS[i];
                if (line.Contains(keyWord))
                {
                    if (line.StartsWith(keyWord) && keyWord != KEYWORDS[3])
                    {
                        return true;
                    }
                    else
                    {
                        int iWord = line.IndexOf(keyWord);
                        List<int> iQuotes = new List<int>();
                        int indQ = 0;
                        while (indQ != -1)
                        {
                            indQ = line.IndexOf("\"", indQ + 1);
                            if (indQ != -1)
                            {
                                iQuotes.Add(i);
                            }
                        }
                        Boolean inQuotes = false;
                        for (int iT = 0; iT < iQuotes.Count; iT++)
                        {
                            inQuotes = iT % 2 != 0;
                            if (inQuotes && iWord < iQuotes[iT])
                            {
                                return true;
                            }
                        }
                        if (!inQuotes)
                        {
                            return true;
                        }
                    }
                }
            }
            return false;
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
            if (iR==-1)
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
