using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SabotageBatchFileProcessor
{
    class FileOp
    {
        public static List<string> loadCodeLinesFromFile(string fileName)
        {
            List<string> lines = new List<string>();
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
            return lines;
        }
    }
}
