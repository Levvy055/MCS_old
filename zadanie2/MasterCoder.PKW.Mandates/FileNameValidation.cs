﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace MasterCoder.PKW.Mandates
{
    public class FileNameValidation
    {
        private static readonly string p = @"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        public static bool Validate(string n)
        {
            n = n.Substring(0,n.Length - 4);
            //By nie porownywac obiektow typu string lepiej uzyc gotowej metody:
            //IPAddress address;
            //if (IPAddress.TryParse(value, out address)&& (address.AddressFamily == AddressFamily.InterNetwork))
            //   { return true; }
            //return false;
            return Regex.IsMatch(n, p);
        }
    }
}
