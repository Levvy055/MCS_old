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
        public static string[] KEYWORDS = { "int", "string", "print", "cast" };
        public static char[] KEYSIGNS = { '(', ')', '+', '-', '*', '=' };

        public BatchInterpreter(string fileName)
        {
            this.fileName = fileName;
            Variables = new Dictionary<string, Variable>();
        }

        public void process()
        {
            lines = FileOp.loadCodeLinesFromFile(fileName);
            processAllLines();
            Console.WriteLine("_________________________________");
            foreach (KeyValuePair<String, Variable> entry in Variables)
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

        private void processAllLines()
        {
            foreach (String line in lines)
            {
                Console.ForegroundColor = ConsoleColor.DarkGreen;
                Console.WriteLine(line);
                Console.ResetColor();
                Boolean isInt;
                if (containsKeyWords(line))
                {
                    if ((isInt = line.StartsWith("int ")) || line.StartsWith("string "))
                    {
                        declareVar(line, isInt);
                    }
                    else if (line.Contains("print"))
                    {
                        prepareToPrint(line);
                    }
                }
                else if (containsKeySign(line, false))
                {
                    calculate(line);
                }
                else if (line.Contains('='))
                {
                    assignValue(line);
                }
            }
        }

        private void calculate(string line)
        {
            Dictionary<Int32, Char> signIndexes = new Dictionary<Int32, Char>();
            for (int i = 0; i < line.Length; i++)
            {
                char c = line[i];
                if (isKeySign(c, false) || c == '"')
                {
                    signIndexes.Add(i, c);
                }
            }
            foreach (KeyValuePair<Int32, Char> entry in signIndexes)
            {
                Console.ForegroundColor = ConsoleColor.Blue;
                Console.WriteLine(entry);
                Console.ResetColor();
            }
        }


        private bool containsKeySign(string line, bool withEqual)
        {
            Boolean inQuotes = false;
            foreach (char ch in line)
            {
                if (ch == '"')
                {
                    if (!inQuotes)
                        inQuotes = true;
                    else
                        inQuotes = false;
                }
                if (KEYSIGNS.Contains(ch) && !inQuotes && (ch != KEYSIGNS[5] || withEqual))
                {
                    return true;
                }
            }
            return false;
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
                        Boolean inQuotes=false;
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

        private bool isKeySign(char c, bool withEqual)
        {
            if (KEYSIGNS.Contains(c) && (c != KEYSIGNS[5] || withEqual))
            {
                return true;
            }
            return false;
        }

        private void declareVar(String line, Boolean isInt)
        {
            int iR = line.IndexOf('=');
            int iL = line.IndexOf(';');
            int iVNL;
            string value;
            Variable variable;
            if (isInt)
            {
                iVNL = line.IndexOf('t');
                variable = new Variable(0);
            }
            else
            {
                iVNL = line.IndexOf('g');
                variable = new Variable("");
            }
            if (line[iVNL + 1] == ' ')
            {
                string varName;
                if (iR == -1)
                {
                    varName = line.Substring(iVNL + 2, iL - iVNL - 2).Trim();
                    Variables.Add(varName, variable);
                }
                else
                {
                    varName = line.Substring(iVNL + 2, iR - iVNL - 2).Trim();
                    value = line.Substring(iR + 1, iL - iR - 1);
                    if (isInt)
                    {
                        int v;
                        if (int.TryParse(value, out v))
                        {
                            variable.IValue = v;
                            Variables.Add(varName, variable);
                        }
                        else
                        {
                            throw new InvalidCastException("Invalid cast exception" + "\n" + value + " in line " + line);
                        }
                    }
                    else
                    {
                        variable.SValue = value;
                        Variables.Add(varName, variable);
                    }
                }
            }
        }

        private void assignValue(string line)
        {
            int iR = line.IndexOf('=');
            int iS = line.IndexOf(';');
            string varName = line.Substring(0, iR).Trim();
            string value = line.Substring(iR + 1, iS - iR - 1);
            int iValue;
            if (value.Contains('"'))
            {
                int iFQ = value.IndexOf('"');
                int sFQ = value.IndexOf('"', iFQ + 1);
                Variables[varName].SValue = value.Substring(iFQ + 1, sFQ - iFQ - 1);
            }
            else if (int.TryParse(value, out iValue))
            {
                Variables[varName].IValue = iValue;
            }
        }

        private void prepareToPrint(string line)
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
