# FinVin - Personal Finance Management App

FinVin is a personal finance management app tailored specifically for students in India. It helps users manage their finances effectively with limited budgets. The app offers budgeting, expense tracking, and financial insights, empowering users to stay on top of their financial health.

## Key Features (Work in progress)

- **Budgeting**: Manage and set budgets based on weekly or monthly income.
- **Expense Analytics**: Visualize spending patterns and get insights on how to save more.
- **Recurring Expenses & Subscriptions**: Track and manage subscriptions, recurring payments, and bills.
- **Multi-Currency Support**: For students dealing with multiple currencies.
- **Income Tracking**: Input and monitor multiple income streams.
- **Receipt Scanning**: Scan and store receipts digitally for record-keeping.
- **Shared Expenses & Group Tracking**: Split bills and expenses easily with friends.
- **Export Data**: Export financial data for external use.
- **Cloud Syncing & Backup**: Secure cloud backups using AWS DynamoDB and AWS Amplify.
- **Notifications & Reminders**: Get notified for upcoming payments and bills.
- **Goals Tracking**: Set and track financial goals.
- **Financial Tips & Insights**: Get personalized financial advice through AI-powered insights.
- **Voice Input for Expenses**: Add expenses with ease using voice commands.
- **Investment Tracking**: Keep track of investments for a holistic financial overview.
- **Customizable Categories**: Organize expenses and income in categories that suit your needs.

## Getting Started (15% implementation)

### Prerequisites
- Android Studio
- Kotlin Programming Language
- AWS Amplify for authentication and cloud storage
- DynamoDB for database management

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/finvin.git
    ```

2. Open the project in Android Studio.

3. Set up AWS Amplify for authentication and cloud storage:
    - Follow the steps to configure AWS Amplify and AWS DynamoDB for user data and backup.

4. Run the project on an Android device or emulator.

### Usage

- **Login/Sign-up**: The app uses AWS Amplify and AWS Cognito for authentication. You can register or sign in with your credentials.
- **Budget Tracking**: After login, input your income (weekly or monthly) and track your daily spending limits.
- **Expense Management**: Easily input expenses, scan receipts, and track recurring payments.
- **Cloud Syncing**: Ensure your data is backed up securely with AWS for future use.


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

FinVin - Making financial management simpler, one student at a time.
