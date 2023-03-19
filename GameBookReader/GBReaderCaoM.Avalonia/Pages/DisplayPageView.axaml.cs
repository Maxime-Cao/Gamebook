using Avalonia.Controls;
using Avalonia.Interactivity;
using GBReaderCaoM.Avalonia.Controls.DisplayPage;
using GBReaderCaoM.Presentations;
using GBReaderCaoM.Presentations.Views;
using System;

namespace GBReaderCaoM.Avalonia.Pages
{
    public partial class DisplayPageView : UserControl, IDisplayPage
    {
        public event EventHandler? GoToHome;
        public event EventHandler<string>? ChoiceSelected;
        public event EventHandler? LoadPageView;
        public event EventHandler? RestartReading;

        public DisplayPageView()
        {
            InitializeComponent();
        }

        public void RefreshView()
        {
            ResetView();
            LoadPageView?.Invoke(this, EventArgs.Empty);
        }

        private void ResetView()
        {
            NumberPage.Text = "";
            TextPage.Text = "";
            ErrorMessage.Text = "";
            SuccessMessage.Text = "";
            RestartReadingButton.IsVisible = false;
            ChoicesPanel.Children.Clear();
        }

        public PageViewModel Page
        {
            set
            {
                if (value != null)
                {
                    ChoicesPanel.Children.Clear();
                    NumberPage.Text = $"Page {value.NumberPage}";
                    TextPage.Text = value.TextPage;
                    RestartReadingButton.IsVisible = value.IsEndPage;
                    foreach (var choice in value.Choices)
                    {
                        var choiceView = new ChoiceView { TextChoice = choice };
                        choiceView.ChoiceSelected += OnChoiceSelected;
                        ChoicesPanel.Children.Add(choiceView);
                    }
                }
            }
        }

        public void OnClickGoToHome(object? sender, RoutedEventArgs args) => GoToHome?.Invoke(this, EventArgs.Empty);

        private void OnChoiceSelected(object? sender, string textChoice) => ChoiceSelected?.Invoke(sender, textChoice);

        private void OnRestartReading(object? sender, RoutedEventArgs args) => RestartReading?.Invoke(this, EventArgs.Empty);

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
