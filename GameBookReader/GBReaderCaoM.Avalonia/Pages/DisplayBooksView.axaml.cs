using Avalonia.Controls;
using Avalonia.Interactivity;
using GBReaderCaoM.Presentations;
using GBReaderCaoM.Presentations.Views;
using System;
using System.Collections.Generic;
using System.Linq;

namespace GBReaderCaoM.Avalonia.Pages
{
    public partial class DisplayBooksView : UserControl, IDisplayBooksView
    {
        private BookDetailsView? _bookDetailsView;

        public event EventHandler<BookViewModel>? BookSelected;
        public event EventHandler<string>? FilterBooks;
        public event EventHandler<string>? StartReading;
        public event EventHandler? LoadBooksView;
        public event EventHandler? GoToStats;

        public DisplayBooksView()
        {
            InitializeComponent();
        }

        public void RefreshView()
        {
            ResetView();
            LoadBooksView?.Invoke(this, EventArgs.Empty);
        }

        public void ResetView()
        {
            ErrorMessage.Text = "";
            SuccessMessage.Text = "";
            BookDetails.Children.Clear();
            BooksView.Children.Clear();
        }

        private void OnClickGoToStats(object? sender, RoutedEventArgs args) => GoToStats?.Invoke(this, EventArgs.Empty);

        public BookDetailsView BookDetailsView
        {
            init
            {
                if (value != null)
                {
                    _bookDetailsView = value;
                    _bookDetailsView.StartReading += OnStartReading;
                }
            }
        }

        public SearchView SearchView
        {
            init
            {
                if (value != null)
                {
                    SearchBook.Children.Clear();
                    SearchBook.Children.Add(value);
                    value.FilterBooks += OnFilterBooks;
                }
            }
        }

        public BookViewModel BookDetail
        {
            set
            {
                if (value != null && _bookDetailsView != null)
                {
                    _bookDetailsView.Book = value;
                    BookDetails.Children.Clear();
                    BookDetails.Children.Add(_bookDetailsView);
                }
            }
        }

        public IEnumerable<BookViewModel> Books
        {
            set
            {
                BooksView.Children.Clear();
                var valueToList = value.ToList();
                if (valueToList == null || valueToList.Count == 0)
                {
                    NoResult.IsVisible = true;
                }
                else
                {
                    foreach (var book in valueToList)
                    {
                        NoResult.IsVisible = false;
                        if (book != null)
                        {
                            GameBookView gameBookView = new() { Book = book };
                            gameBookView.BookSelected += OnBookSelected;
                            BooksView.Children.Add(gameBookView);
                        }
                    }
                }
            }
        }

        private void OnBookSelected(object? sender, BookViewModel book) => BookSelected?.Invoke(sender, book);

        private void OnFilterBooks(object? sender, string filter) => FilterBooks?.Invoke(sender, filter);

        private void OnStartReading(object? sender, string isbn) => StartReading?.Invoke(sender, isbn);


        public void DisplayError(string error)
        {
            ErrorMessage.Text = error;
            ErrorMessage.IsVisible = true;
            SuccessMessage.IsVisible = false;
        }

        public void DisplayInfo(string info)
        {
            SuccessMessage.Text = info;
            ErrorMessage.IsVisible = false;
            SuccessMessage.IsVisible = true;
        }

        public void HideChildren()
        {
            SearchBook.IsVisible = false;
            SelectedBookTitle.IsVisible = false;
            BooksTitle.IsVisible = false;
            NoResult.IsVisible = false;
            StatsButton.IsVisible = false;
        }
    }
}
