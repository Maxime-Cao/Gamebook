using Avalonia.Controls;
using Avalonia.Interactivity;
using GBReaderCaoM.Avalonia.Controls.DisplayStats;
using GBReaderCaoM.Presentations;
using GBReaderCaoM.Presentations.Views;
using System;
using System.Collections.Generic;
using System.Linq;

namespace GBReaderCaoM.Avalonia.Pages
{
    public partial class DisplayStatsView : UserControl, IDisplayStatsView
    {

        public event EventHandler? GoToHome;
        public event EventHandler? LoadStats;
        public DisplayStatsView()
        {
            InitializeComponent();
        }

        public void RefreshView()
        {
            ResetView();
            LoadStats?.Invoke(this, EventArgs.Empty);
        }

        private void ResetView()
        {
            StatsTitle.Text = "Nombre de livres en cours de lecture : 0";
            StatsView.Children.Clear();
            ErrorMessage.Text = "";
            SuccessMessage.Text = "";
        }

        public IEnumerable<StatViewModel> Stats
        {
            set
            {
                var valueToList = value.ToList();
                if (valueToList != null)
                {
                    StatsView.Children.Clear();
                    StatsTitle.Text = $"Nombre de livres en cours de lecture : {valueToList.Count}";
                    foreach (var statViewModel in valueToList)
                    {
                        StatsView.Children.Add(new StatView { Stat = statViewModel });
                    }
                }
            }
        }

        public void OnClickGoToHome(object? sender, RoutedEventArgs args) => GoToHome?.Invoke(this, EventArgs.Empty);

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
    }
}
