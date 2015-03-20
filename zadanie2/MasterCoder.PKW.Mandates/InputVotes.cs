using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{
    internal class InputVotes : IInputVotes
    {
        private List<Vote> v { get; set; }
        private string FilePath { get; set; }

        public InputVotes(string votesFilesPath)
        {
            v = new List<Vote>();
            FilePath = votesFilesPath;
            Initialize();
        }

        private void Initialize()
        {
            string[] f = Directory.GetFiles(FilePath);
            //Dla lepszej optymalizacji najlepiej odczyt kazdego pliku wykonac w oddzielnym watku dla duzej ilosci danych
            for (int i = 0; i < f.Length; i++)
            {
                if (FileNameValidation.Validate(Path.GetFileName(f[i])))
                {
                    string[] l = File.ReadAllLines(f[i]); 
                    InitializeValidVotesFromFileData(l);
                }
            }
        }

        private void InitializeValidVotesFromFileData(string[] l)
        {
            for(int i=0; i<l.Length;i++)
            {
                var f = l[i].Split(';');
                var vote = new Vote(f[0], f[1], Int32.Parse(f[2]));//lepiej uzyc TryParse(string), poniewaz nie wiemy czy dane sa prawidlowe 
                                                                    // i obsluzyc odpowiednio 'if', jak w przypadku adresu IP
                
                bool updated = false;

                for (int j = 0; j < v.Count(); j++)
                {
                    if(v[j].PartShortName==vote.PartShortName)
                    {
                        v[j].ValidVotes += vote.ValidVotes; // dodajemy glosy do siebie, z poprzedniego okregu glosy inaczej zostana nadpisane
                        updated = true;
                        break;
                    }
                }

                if (!updated)
                    v.Add(vote);
            }
        }

        public List<Vote> GetAllValidVotes()
        {
            return v;
        }

        public List<Vote> GetAllValidVotesForPart(string partShortName)
        {
            return v.Where(v1 => v1.PartShortName == partShortName).ToList();
        }
    }
}
