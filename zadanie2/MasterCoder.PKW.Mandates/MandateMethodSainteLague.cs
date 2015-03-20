using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{
    internal class MandateMethodSainteLague : IMandateMethod
    {
        public List<Mandate> CalculateMandates(List<Vote> votes, int mandatesCount)
        {
            List<Mandate> partMandates = Init(votes);

            List<float> calcTab = new List<float>(votes.Select(x => (float)x.ValidVotes));

            int maxInd = 0;

            for (int i = mandatesCount; i > 0; i--) // malo czytelne ze odwrocona kolejnosc i  >= ->> >, bo by byl o 1 wiecej mandat
            {
                float m = -1;
                for (int j = 0; j < calcTab.Count; j++) //znajduje max
                {
                    if (m < calcTab[j]) 
                    {
                        m = calcTab[j];
                        maxInd = j;
                    }
                }

                partMandates[maxInd].Mandates++;
                calcTab[maxInd] = Calc(votes[maxInd].ValidVotes, partMandates[maxInd].Mandates);
            }

            return partMandates;
        }

        private List<Mandate> Init(List<Vote> v)
        {
            List<Mandate> partMandates = new List<Mandate>(v.Count);

            foreach(var vote in v)
            {
                var mandate = new Mandate(vote.PartShortName, vote.PartName, 0);
                partMandates.Add(mandate);
            }

            return partMandates;
        }

        private float Calc(int v, int mandates)
        {
            if (mandates == 0) // dla ilosci 0 mandatow zwracana jest wartosc (v/1.4)
            {
                return (float)((float)v / 1.4);
            }
            return (float)((float)v / (2*mandates + 1.0)); // brakujace 2* poniewaz metoda powinna zwracac wartosc v/(2*mandates+1) dla mandatow > 0
        }
    }
}
