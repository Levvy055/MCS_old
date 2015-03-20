using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{
    internal class InputParametersException : Exception
    {
        public InputParametersException(string message)
            : base(message)
        {
        }
    }
}
