### Next-generation, component-first structure (from DOCTYPE prototype)

Principles from DOCTYPE.html applied: Tab-based app shell, themed via tokens and [data-theme] variants (rose, emerald, ocean, sunset, galaxy), dark mode, bottom sheet system, carousel, toast, story templates, store/cart, events filters, notifications, and persisted app state.

```txt
client/
  src/
    app/
      app/
        App.tsx                     # single app root (matches current project style)
      providers/                    # one-time global composition
        NavigationProvider.tsx
        ThemeProvider.tsx           # dark mode + theme variant
        StateProvider.tsx           # store setup + persistence
        GestureProvider.tsx
        SafeAreaProvider.tsx
        index.tsx

      navigation/
        tabs/
          TabNavigator.tsx          # tabs: home, faculties, create, events, profile
          tabs.ts                    # tab config (icon, label, route)
        stacks/
          AuthStack.tsx             # login, register wizard, forgot password
          HomeStack.tsx
          EventsStack.tsx
          ProfileStack.tsx
        modals/
          ModalHost.tsx
          SheetHost.tsx             # bottom sheet portal/host
        linking.ts
        routes.ts                   # central route names

      styles/
        theme/
          tokens/
            colors.ts               # primary/accent/success/warning + dark set
            spacing.ts
            radius.ts
            shadows.ts
            typography.ts
          variants/                 # premium setup theme variants
            rose.ts
            emerald.ts
            ocean.ts
            sunset.ts
            galaxy.ts
          schemes/
            light.ts
            dark.ts
          index.ts                  # ThemeContext, useTheme(), useThemeVariant()

      components/
        primitives/
          Avatar.tsx
          Badge.tsx
          Button.tsx
          Card.tsx
          Divider.tsx
          Icon.tsx
          Image.tsx
          Input.tsx
          Spinner.tsx
          Typography.tsx
        navigation/
          TabBar.tsx                # custom tab bar from prototype
          Header.tsx
          FAB.tsx
        overlays/
          Modal.tsx
          Sheet.tsx                 # bottom sheet primitive (collapsed/expanded)
          ActionSheet.tsx
          Toast.tsx                 # toast primitive
        media/
          Carousel.tsx              # carousel logic (dots, prev/next)
          VideoPlayer.tsx
        composite/
          PostCard/
            PostCard.tsx
            PostMedia.tsx           # image | video | carousel
            PostActions.tsx
          StoryRing.tsx
          ProductCard.tsx
          EventCard.tsx
          ProfileHeader.tsx
          SettingsList.tsx

      features/
      auth/
        screens/
          LoginScreen.tsx
          ForgotPasswordScreen.tsx
          RegisterWizard.tsx            # orchestrator with paged flow
        pager/
          RegistrationPager.tsx         # swipe/pager controller (RTL-aware)
        steps/                          # multi-step registration
          StepAccountInfo.tsx           # email/phone + password
          StepProfileDetails.tsx        # name, avatar, bio
          StepUniversity.tsx            # school/faculty, student id (optional)
          StepPreferences.tsx           # interests/tags, notifications
          StepReview.tsx                # summary + submit
        components/
          RegistrationStepper.tsx       # visual stepper/indicators
          RegistrationProgress.tsx      # progress bar
          RegistrationFooter.tsx        # next/back/submit CTA bar
        hooks/
          useRegistrationWizard.ts      # step logic, guards, progress calc
        services/
          auth.api.ts
        store/
          auth.store.ts
          registration.store.ts         # step state + form data + persistence
        validation/
          schemas.ts                    # zod/yup schemas per step
        types.ts                        # RegistrationPayload, StepId, etc.

      faculties/
        screens/
          FacultiesScreen.tsx
          FacultyDetailScreen.tsx
        services/faculties.api.ts
        store/faculties.store.ts

      create/
        components/StoryCreatorSheet.tsx
        components/PostComposerSheet.tsx
        components/MediaPickerSheet.tsx
        flows/create-launcher.tsx         # entry for the Create tab
        store/create.store.ts

      feed/
        screens/
          HomeScreen.tsx
          PostDetailScreen.tsx
        components/PostComposer.tsx
        services/posts.api.ts
        services/analytics.api.ts # rankings, insights hooks
        store/feed.store.ts

      stories/
        screens/StoryViewerScreen.tsx
        components/StoryCreatorSheet.tsx
        components/StoryTemplatesGrid.tsx
        templates/story-templates.ts  # ootd, mood, study, event
        services/stories.api.ts
        store/stories.store.ts

      search/
        screens/SearchScreen.tsx
        services/search.index.ts        # buildSearchIndex + query helpers
        store/search.store.ts

      store/
        screens/StoreScreen.tsx
        screens/ProductDetailScreen.tsx
        components/CartButton.tsx
        services/products.api.ts
        store/cart.store.ts
        store/products.store.ts

      events/
        screens/EventsScreen.tsx
        screens/EventDetailScreen.tsx
        services/events.api.ts
        store/events.store.ts

      profile/
        screens/ProfileScreen.tsx
        screens/EditProfileScreen.tsx
        screens/SettingsScreen.tsx
        screens/AnalyticsScreen.tsx
        services/profile.api.ts
        services/notifications.api.ts
        store/profile.store.ts

      notifications/
        screens/NotificationsScreen.tsx
        store/notifications.store.ts

      inbox/
        screens/InboxScreen.tsx
        screens/ChatScreen.tsx
        components/ChatHeader.tsx
        services/inbox.api.ts
        store/inbox.store.ts

      connections/
        components/ConnectionsSheet.tsx
        services/connections.api.ts
        store/connections.store.ts

      error/
        screens/ErrorBoundary.tsx
        screens/ErrorDetails.tsx

      services/
        http/
          apiClient.ts              # base client using Expo extra.apiBaseUrl
          interceptors.ts
        realtime/
          socketClient.ts
        storage/
          asyncStorage.ts
          secureStore.ts
        analytics/
          telemetry.ts              # OTEL/Grafana-compatible hooks

      store/
        index.ts                    # export stores/selectors
        persist.ts                  # AsyncStorage persistence config
        middleware/
          logger.ts
          errorMonitor.ts

      config/
        env.ts                      # reads Expo extras from app.config.ts
        featureFlags.ts

      hooks/
        useToast.ts
        useBottomSheet.ts
        useCarousel.ts
        useTheme.ts
        useFeatureFlags.ts

      utils/
        date.ts
        validation.ts
        format.ts
        array.ts
        device.ts
        navigation.ts

      constants/
        routes.ts
        featureFlags.ts

      types/
        index.ts                    # shared types and DTO typings

      assets/
        images/
        icons/
        svgs/
        fonts/
```

Implementation notes
- Navigation: tabs (home, faculties, create, events, profile) with per-tab stacks; `SheetHost` for global bottom-sheets.
- Theming: tokens + light/dark schemes + premium variants; `ThemeProvider` exposes current variant and dark mode toggle.
- Templates: story templates live under `features/stories/templates` and are rendered by `StoryTemplatesGrid`.
- State: colocated feature stores (e.g., `feed.store.ts`) plus a shared `store/persist.ts` to mirror prototype persistence.
- Services: `services/http/apiClient.ts` reads `apiBaseUrl` from Expo extras; feature services wrap endpoints (auth, posts, products, events, profile, notifications, analytics).
- Components: primitives → overlays/media/navigation → composite widgets (PostCard, ProductCard, EventCard) used by screens.

