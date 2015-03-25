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

        public SBFP(string fileName)
        {
            this.fileName = fileName;
            lines = new List<string>();
        }

        public void process()
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

            foreach (String line in lines)
            {
                Console.WriteLine(line);
            }
        }
    }
}
