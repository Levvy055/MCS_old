using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SabotageBatchFileProcessor
{
    public class StringParser
    {
        private BatchInterpreter batchInterpreter;

        public StringParser(BatchInterpreter batchInterpreter)
        {
            this.batchInterpreter = batchInterpreter;
        }

        public bool containsKeySigns(string line, bool withEqual)
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
                if (BatchInterpreter.KEYSIGNS.Contains(ch) && !inQuotes && (ch != BatchInterpreter.KEYSIGNS[5] || withEqual))
                {
                    return true;
                }
            }
            return false;
        }

        public bool ContainsKeyWords(string line)
        {
            for (int i = 0; i < BatchInterpreter.KEYWORDS.Length; i++)
            {
                string keyWord = BatchInterpreter.KEYWORDS[i];
                if (ContainsKeyWord(line, keyWord))
                {
                    return true;
                }
            }
            return false;
        }

        public bool ContainsKeyWord(string line, string keyWord)
        {
            if (line.Contains(keyWord))
            {
                if (line.StartsWith(keyWord) && keyWord != BatchInterpreter.KEYWORDS[3])
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
                            iQuotes.Add(indQ);
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
            return false;
        }

        public void assignValue(string line)
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
                batchInterpreter.assignValue(varName, value.Substring(iFQ + 1, sFQ - iFQ - 1));
            }
            else if (int.TryParse(value, out iValue))
            {
                batchInterpreter.assignValue(varName, iValue);
            }
            else
            {
                throw new InvalidCastException("Invalid Cast Exception while casting " + value);
            }
        }

        public Dictionary<int, char> getSignIndexes(string line)
        {
            Dictionary<Int32, Char> signIndexes = new Dictionary<Int32, Char>();
            int opB = 0;
            int clB = 0;
            for (int i = 0; i < line.Length; i++)
            {
                char c = line[i];
                if (isKeySign(c, true) || c == '"')
                {
                    signIndexes.Add(i, c);
                    if (c == BatchInterpreter.KEYSIGNS[0])
                    {
                        opB++;
                    }
                    else if (c == BatchInterpreter.KEYSIGNS[1])
                    {
                        clB++;
                    }
                }
            }
            if (opB != clB) { throw new Exception("Invalid brackets count! in line " + line); }
            return signIndexes;
        }

        private bool isKeySign(char c, bool withEqual)
        {
            if (BatchInterpreter.KEYSIGNS.Contains(c) && (c != BatchInterpreter.KEYSIGNS[5] || withEqual))
            {
                return true;
            }
            return false;
        }
    }
}
