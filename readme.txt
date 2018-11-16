API: https://www.wattpad.com/api/v3/stories?offset=0&limit=30&fields=stories(id,title,cover,user)

The 'Wattpad Stories' app uses this API to display a feed of stories with each story displaying the title, cover image and author.
The list is 'infinitely' scrollable, using pagination to pull 30 stories at a time. The feed begins pulling the next 30 stories once
a user scrolls to within 5 stories of the last item.

The search feature was implemented more as a 'filter' and aims only to filter results which have already been populated locally. To ensure the search feature is only used as a local filter, it will not make any API requests while the SearchView is expanded. So, if you want more results to search through, first 'X' out of the SearchView, then you can scroll for new results once again. The reason for this decision is the lack of a search endpoint in the API, and a more thorough search through the given query is infeasible.
