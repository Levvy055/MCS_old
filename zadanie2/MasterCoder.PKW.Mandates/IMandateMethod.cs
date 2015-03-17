using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{   
    interface IMandateMethod
    {
        List<Mandate> CalculateMandates(List<Vote> votes, int mandates);
    }
}
