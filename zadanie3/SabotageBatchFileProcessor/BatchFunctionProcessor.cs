using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SabotageBatchFileProcessor
{
    public class BatchFunctionProcessor
    {
        public static void print(string s)
        {
            Console.WriteLine(s);
        }

        public static void print(int i)
        {
            Console.WriteLine(i);
        }

        public static int cast(string s)
        {
            int i;
            if (int.TryParse(s, out i))
            {
                return i;
            }
            else
            {
                throw new InvalidCastException("Invalid Cast Exception while casting '" + s + "'");
            }
        }

        public static string cast(int i)
        {
            return i + "";
        }
    }
}
