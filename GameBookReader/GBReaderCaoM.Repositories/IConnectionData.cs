using System.Data.Common;

namespace GBReaderCaoM.Repositories
{
    public interface IConnectionData
    {
        public string ProviderName { get; }

        public string DataSource { get; }

        public string UserId { get; }

        public string Password { get; }

        public string DBName { get; }

        public DbProviderFactory DbProviderFactory { get; }
    }
}
