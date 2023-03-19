using Avalonia.Controls;
using Avalonia.Interactivity;
using System;

namespace GBReaderCaoM.Avalonia.Controls.DisplayPage
{
    public partial class ChoiceView : UserControl
    {
        public event EventHandler<string>? ChoiceSelected;
        public ChoiceView()
        {
            InitializeComponent();
        }

        public string TextChoice
        {
            init => ChoiceButton.Content = value;
        }

        public void OnClickChoice(object? sender, RoutedEventArgs args)
        {
            string textButton = ChoiceButton.Content as string ?? "";
            ChoiceSelected?.Invoke(this, textButton);
        }
    }
}
