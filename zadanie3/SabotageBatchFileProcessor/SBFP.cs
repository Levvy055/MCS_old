using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SabotageBatchFileProcessor
{
    class SBFP
    {
        private string fileName;
        private List<string> lines;
        private Dictionary<string, string> strVariables;
        private Dictionary<string, int> intVariables;
        private String[] KEYWORDS = { "int", "string", "print", "cast" };

        public SBFP(string fileName)
        {
            this.fileName = fileName;
            lines = new List<string>();
            strVariables = new Dictionary<string, string>();
            intVariables = new Dictionary<string, int>();
        }

        public void process()
        {
            loadCodeLinesFromFile();
            processAllLines();
            foreach (KeyValuePair<string, int> entry in intVariables)
            {
                Console.WriteLine("i " + entry);
            }
            foreach (KeyValuePair<string, string> entry in strVariables)
            {
                Console.WriteLine("s " + entry);
            }
        }

        private void loadCodeLinesFromFile()
        {
            string[] linesT = System.IO.File.ReadAllLines(fileName);
            string befLine = "";
            foreach (string line in linesT)
            {
                if (line.Contains(';'))
                {
                    if (befLine == "")
                    {
                        lines.Add(line);
                    }
                    else
                    {
                        lines.Add(befLine + line); befLine = "";
                    }
                }
                else if (line.Length > 1)
                {
                    befLine += line;
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
            if (isInt)
            {
                iVNL = line.IndexOf('t');
            }
            else
            {
                iVNL = line.IndexOf('g');
            }
            if (line[iVNL + 1] == ' ')
            {
                string varName;
                if (iR == -1)
                {
                    varName = line.Substring(iVNL + 2, iL - iVNL - 2).Trim();
                    if (isInt)
                    {
                        intVariables.Add(varName, 0);
                    }
                    else
                    {
                        strVariables.Add(varName, "");
                    }
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
                            intVariables.Add(varName, v);
                        }
                        else
                        {
                            throw new InvalidCastException("Invalid cast exception" + "\n" + value + " in line " + line);
                        }
                    }
                    else
                    {
                        strVariables.Add(varName, value);
                    }
                }
            }
        }

        private void assignValue(String line)
        {
            int iR = line.IndexOf('=');
            int iS = line.IndexOf(';');
            string varName = line.Substring(0, iR).Trim();
            string value = line.Substring(iR + 1, iS - iR - 1);
            int iValue;
            if (value.Contains('"') && strVariables.ContainsKey(varName))
            {
                int iFQ = value.IndexOf('"');
                int sFQ = value.IndexOf('"', iFQ + 1);
                strVariables[varName] = value.Substring(iFQ + 1, sFQ - iFQ - 1);
            }
            else if (intVariables.ContainsKey(varName) && int.TryParse(value, out iValue))
            {
                intVariables[varName] = iValue;
            }
        }
    }
}
