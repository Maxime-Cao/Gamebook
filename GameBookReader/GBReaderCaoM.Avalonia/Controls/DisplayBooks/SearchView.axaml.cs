using Avalonia.Controls;
using Avalonia.Interactivity;
using System;

namespace GBReaderCaoM.Avalonia
{
    public partial class SearchView : UserControl
    {
        public event EventHandler<string>? FilterBooks;
        public SearchView()
        {
            InitializeComponent();
        }

        public void OnClickSearchBook(object? sender, RoutedEventArgs args)
        {
            string searchBookInput = SearchBookInput.Text;

            FilterBooks?.Invoke(sender, searchBookInput);
        }
    }
}
