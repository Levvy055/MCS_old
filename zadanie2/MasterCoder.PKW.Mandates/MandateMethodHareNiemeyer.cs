using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{
    class MandateMethodHareNiemeyer : IMandateMethod
    {
        public List<Mandate> CalculateMandates(List<Vote> votes, int mandatesCount)
        {
            var v = votes;
            List<Mandate> partMandates = Init(v);

            List<double> tab = new List<double>(v.Count);
            var totalVotes = v.Sum(x => x.ValidVotes);

            int mandCounter=0;
            for(int i=0; i<v.Count;i++)
            {
                tab.Add(v[i].ValidVotes * mandatesCount / totalVotes);
                partMandates[i].Mandates = (int)Math.Floor(tab[i]);
                tab[i] -= partMandates[i].Mandates;
                mandCounter += partMandates[i].Mandates;
            }

            double max;
            int maxInd=0;

            while(mandCounter<mandatesCount)
            {
                max = -1;
                for(int i=0; i<v.Count; i++)
                {
                    if(max<tab[i])
                    {
                        max = tab[i];
                        maxInd = i;
                    }
                }

                partMandates[maxInd].Mandates++;
                mandCounter++;
                tab[maxInd] = 0;
            }

            return partMandates;
        }

        private List<Mandate> Init(List<Vote> votes)
        {
            List<Mandate> partMandates = new List<Mandate>(votes.Count);

            foreach(var vote in votes)
            {
                var mandate = new Mandate(vote.PartShortName, vote.PartName, 0);
                partMandates.Add(mandate);
            }

            return partMandates;
        }
    }
}
