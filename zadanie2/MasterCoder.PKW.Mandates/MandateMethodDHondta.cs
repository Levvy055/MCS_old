using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{
    internal class MandateMethodDHondta : IMandateMethod
    {
        public List<Mandate> CalculateMandates(List<Vote> votes, int mandatesCount)
        {
            var v = votes;
            List<Mandate> partMandates = InitializeMandates(v);

            List<double> tab = new List<double>(v.Select(x => (double)x.ValidVotes));

            int maxInd = 0;

            for (int i = mandatesCount; i > 0; i--)
            {
                double max = -1;
                for (int j = 0; j < tab.Count; j++)
                {
                    if (max < tab[j])
                    {
                        max = tab[j];
                        maxInd = j;
                    }
                }

                partMandates[maxInd].Mandates++;
                tab[maxInd] = Calc(v[maxInd].ValidVotes, partMandates[maxInd].Mandates);
            }

            return partMandates;
        }

        private List<Mandate> InitializeMandates(List<Vote> v)
        {
            List<Mandate> partMandates = new List<Mandate>(v.Count);

            foreach(var vote in v)
            {
                var mandate = new Mandate(vote.PartShortName, vote.PartName, 0);
                partMandates.Add(mandate);
            }

            return partMandates;
        }

        private double Calc(int v, int mandates)
        {
            return ((double)v * 1.0 / (2*mandates + 1.0));
        }
    }
}
