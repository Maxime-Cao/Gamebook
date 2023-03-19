using Avalonia.Controls;
using GBReaderCaoM.Presentations.Views;
using System.Collections.Generic;

namespace GBReaderCaoM.Avalonia
{
    public partial class MainWindow : Window, ISwitchToPages
    {
        private readonly IDictionary<string, IPage> _pages = new Dictionary<string, IPage>();
        public MainWindow()
        {
            InitializeComponent();
        }

        public void AddPage(string pageName, IPage page) => _pages[pageName] = page;

        public void SwitchToPage(string pageName)
        {
            _pages[pageName].RefreshView();
            Content = _pages[pageName];
        }
    }
}
