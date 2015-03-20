using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{
    static class MandateMethodFactory
    {
        public static IMandateMethod Get(MethodEnum method)
        {
            switch (method)
            {
                case MethodEnum.DHondta:
                    return new MandateMethodDHondta();
                case MethodEnum.SaintLague:
                    return new MandateMethodSainteLague();
                case MethodEnum.HareNiemeyer:
                    return new MandateMethodHareNiemeyer();
                default:
                    return new MandateMethodDHondta();
            }
        }
    }
}
