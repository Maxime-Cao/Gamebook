using Avalonia.Controls;
using Avalonia.Input;
using GBReaderCaoM.Presentations;
using System;

namespace GBReaderCaoM.Avalonia
{
    public partial class GameBookView : UserControl
    {
        private BookViewModel? _bookViewModel;
        public event EventHandler<BookViewModel>? BookSelected;
        public GameBookView()
        {
            InitializeComponent();
        }

        public BookViewModel? Book
        {
            init
            {
                if (value != null)
                {
                    _bookViewModel = value;
                    TitleBook.Text = value.TitleBook;
                    AuthorBook.Text = $"Auteur : {value.AuthorName}";
                    IsbnBook.Text = $"ISBN : {value.Isbn}";
                }
            }

            get => _bookViewModel;
        }

        private void OnBookPanelClick(object? sender, PointerPressedEventArgs args)
        {
            if (_bookViewModel is not null)
            {
                BookSelected?.Invoke(this, _bookViewModel);
            }
        }
        private void OnMouseEnterBookPanel(object? sender, PointerEventArgs args) => BookPanel.Opacity = 0.5;

        private void OnMouseLeaveBookPanel(object? sender, PointerEventArgs args) => BookPanel.Opacity = 1;

    }
}
