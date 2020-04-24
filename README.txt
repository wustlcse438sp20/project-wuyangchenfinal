App Name: MyRecipe
Team Members: Haoyu Wu
              Ruoyao Yang
              Jialun Chen

In this file you should include:

Any information you think we should know about your submission

It can't be said issues but some imperfect performances because of APIs.
The first is that imageUrl returned by API may be null, so we do judgement and using another image when it returns an empty imageUrl.
The second is that smart meal page has no photho shown with recipes because the API used to return the imageUrl but now there is no imageUrl.
