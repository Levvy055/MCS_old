using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SabotageBatchFileProcessor
{
    public class Variable
    {
        private int iValue;
        private string sValue;

        public Variable(int value)
        {
            iValue = value;
            VariableType = VariableTypes.INT;
        }

        public Variable(string value)
        {
            sValue = value;
            VariableType = VariableTypes.STRING;
        }

        public VariableTypes VariableType { get; private set; }
        public int IValue
        {
            get
            {
                return iValue;
            }
            set
            {
                if (VariableType == VariableTypes.INT)
                {
                    iValue = value;
                };
            }
        }
        public string SValue
        {
            get
            {
                return sValue;
            }
            set
            {
                if (VariableType == VariableTypes.STRING)
                {
                    sValue = value;
                };
            }
        }
    }

    public enum VariableTypes
    {
        STRING, INT
    }
}
