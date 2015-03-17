using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{
    internal sealed class Vote
    {
        public string PartShortName { get; set; }
        public string PartName { get; set; }
        public int ValidVotes { get; set; }

        public Vote(string partShortName, string partName, int validVotes)
        {   
            PartShortName = partShortName.ToUpper();
            PartName = partName;
            ValidVotes = validVotes;
        }
    }
}
