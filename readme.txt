API: https://www.wattpad.com/api/v3/stories?offset=0&limit=30&fields=stories(id,title,cover,user)

The 'Wattpad Stories' app uses this API to display a feed of stories with each story displaying the title, cover image and author.
The list is 'infinitely' scrollable, using pagination to pull 30 stories at a time. The feed begins pulling the next 30 stories once
a user scrolls to within 5 stories of the last item. Although all User data is stored, only the username is currently used;
displaying both the user avatar and a cover photo for each story would quickly become cumbersome, both visually and computationally.

The search feature was implemented more as a 'filter' and aims only to filter results which have already been populated locally. 
Any sort of exhaustive search through the given API would put unneccessary load on the server while also being slow, 
since it would require multiple network requests and the app would have to process the entire block of JSON for each.
Any more thorough search feature would be better implemented on the API end of things so that only one network call is needed.

To ensure the search feature is only used as a local filter, it will not make any API requests while the SearchView is expanded.
So, if you want more results to search through, first 'X' out of the SearchView, then you can scroll for new results once again.

For your convenience, a debug APK has been included along with this readme, though you can compile the app yourself as normal.

Thank you, I look forward to any criticism or feedback.