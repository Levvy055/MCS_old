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
        public static String[] KEYWORDS = { "int", "string", "print", "cast" };
        public static String[] KEYSIGNS = { "(", ")", "+", "-", "*", "=" };

        public BatchInterpreter(string fileName)
        {
            this.fileName = fileName;
            Variables = new Dictionary<string, Variable>();
        }

        public void process()
        {
            lines = FileOp.loadCodeLinesFromFile(fileName);
            processAllLines();
            Console.WriteLine("");
            foreach (KeyValuePair<String, Variable> entry in Variables)
            {
                if (entry.Value.VariableType == VariableTypes.INT)
                {
                    Console.WriteLine("i [" +entry.Key+", "+ entry.Value.IValue+"]");
                }
                else
                {
                    Console.WriteLine("s [" +entry.Key+", "+ entry.Value.SValue+"]");
                }
            }
        }

        private void processAllLines()
        {
            foreach (String line in lines)
            {
                Console.WriteLine(line);
                Boolean isInt;
                if (containsKeyWords(line))
                {
                    if ((isInt = line.StartsWith("int ")) || line.StartsWith("string "))
                    {
                        declareVar(line, isInt);
                    }
                }
                else if (line.Contains('='))
                {

                    assignValue(line);
                }
            }
        }

        private bool containsKeyWords(string line)
        {
            for (int i = 0; i < KEYWORDS.Length; i++)
            {
                string keyWord = KEYWORDS[i];
                if (line.Contains(keyWord))
                {
                    if (((i == 0 || i == 1) && line.StartsWith(keyWord)) || i > 1)
                    {
                        return true;
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
    }
}
