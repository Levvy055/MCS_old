using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SabotageBatchFileProcessor
{
    public static class FileOp
    {
        public static List<String> loadCodeLinesFromFile(string fileName)
        {
            List<String> lines = new List<String>();
            string[] linesT = System.IO.File.ReadAllLines(fileName);
            string befLine = "";
            foreach (string line in linesT)
            {
                if (line.Contains(';'))
                {
                    if (befLine == "")
                    {
                        if (ContainsAny(line, ToArray(BatchInterpreter.KEYSIGNS), BatchInterpreter.KEYWORDS))
                        {
                            lines.Add(line);
                        }
                    }
                    else
                        if (ContainsAny(befLine + line, ToArray(BatchInterpreter.KEYSIGNS), BatchInterpreter.KEYWORDS))
                        {
                            lines.Add(befLine + line);
                        }
                    befLine = "";
                }
                else if (line.Length > 1)
                {
                    befLine += line;
                }
            }
            return lines;
        }

        public static bool ContainsAny(this string word, params string[][] arrays)
        {
            foreach (string[] array in arrays)
            {
                foreach (string nWord in array)
                {
                    if (word.Contains(nWord))
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        public static string[] ToArray(char[] array)
        {
            string[] cA=new string[array.Length];
            for (int i=0;i<array.Length;i++)
            {
                cA[i] = array[i].ToString();
            }
            return cA;
        }
    }
}
