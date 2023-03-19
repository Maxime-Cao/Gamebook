using GBReaderCaoM.Repositories;
using System.Data.Common;

namespace GBReaderCaoM.Infrastructures.DB
{
    public class MySQLConnectionData : IConnectionData
    {
        public string ProviderName => "MySql.Data.MySqlClient";

        public string DataSource => "192.168.128.13";

        public string UserId => "in20b1054";

        public string Password => "8319";

        public string DBName => "in20b1054";

        public DbProviderFactory DbProviderFactory => MySql.Data.MySqlClient.MySqlClientFactory.Instance;
    }
}
