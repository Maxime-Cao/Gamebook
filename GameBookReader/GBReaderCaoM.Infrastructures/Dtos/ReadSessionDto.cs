namespace GBReaderCaoM.Infrastructures.Dtos
{
    public record ReadSessionDto
    {
        private readonly IList<UserReadSessionDto>? _sessions;

        public ReadSessionDto(IList<UserReadSessionDto> sessions)
        {
            if (sessions != null)
            {
                _sessions = new List<UserReadSessionDto>(sessions);
            }
        }

        public IList<UserReadSessionDto>? Sessions => _sessions;
    }
}
