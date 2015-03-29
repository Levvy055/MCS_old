using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SabotageBatchFileProcessor
{
    public class Program
    {
        public static void Main(string[] args)
        {
            if (args.Length != 1)
            {
                throw new IOException("Bad arguments amount!");
            }
            if (!File.Exists(args[0]))
            {
                throw new IOException("File " + args[0] + " not found.");
            }
            BatchInterpreter sBFP = new BatchInterpreter(args[0]);
            try
            {
                sBFP.process();
            }
            catch (InvalidCastException e)
            {
                Console.WriteLine("Invalid Cast Exception\n" + e.Message);
            }
            catch (NullReferenceException e)
            {
                Console.WriteLine(e.Message);
            }
            catch (InvalidOperationException e)
            {
                Console.WriteLine(e.Message);
            }
            catch (ArgumentException e)
            {
                Console.WriteLine(e.Message);
            }
            Console.ReadLine();
        }
    }
}
