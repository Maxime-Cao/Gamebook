using Avalonia.Controls;
using GBReaderCaoM.Presentations;

namespace GBReaderCaoM.Avalonia.Controls.DisplayStats
{
    public partial class StatView : UserControl
    {
        public StatView()
        {
            InitializeComponent();
        }

        public StatViewModel Stat
        {
            init
            {
                if (value != null)
                {
                    IsbnBook.Text = $"ISBN du livre : {value.IsbnBook}";
                    TitleBook.Text = $"Titre du livre : {value.TitleBook}";
                    CurrentPage.Text = $"Page actuelle : {value.NumberPage}";
                    FirstRead.Text = $"Début de la lecture : {value.FirstRead}";
                    LastRead.Text = $"Dernière lecture : {value.LastRead}";
                }
            }
        }
    }
}
