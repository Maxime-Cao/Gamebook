using Avalonia.Controls;
using Avalonia.Interactivity;
using GBReaderCaoM.Presentations;
using System;

namespace GBReaderCaoM.Avalonia
{
    public partial class BookDetailsView : UserControl
    {
        public event EventHandler<string>? StartReading;
        private BookViewModel? _bookModel;
        public BookDetailsView()
        {
            InitializeComponent();
        }

        public BookViewModel Book
        {
            set
            {
                if (value != null)
                {
                    _bookModel = value;
                    TitleBook.Text = value.TitleBook;
                    AuthorBook.Text = $"Auteur : {value.AuthorName}";
                    IsbnBook.Text = $"ISBN : {value.Isbn}";
                    ResumeBook.Text = value.Resume;
                }
            }

        }
        private void OnClickStartReading(object sender, RoutedEventArgs args)
        {
            if (_bookModel != null)
            {
                StartReading?.Invoke(sender, _bookModel.Isbn);
            }
        }
    }
}
