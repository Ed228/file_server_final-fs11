import React, {lazy, Suspense, useMemo} from 'react'
import {useSelector} from 'react-redux'
import {Switch} from 'react-router-dom'

import MainLayout from '@layouts/MainLayout'
import {PageLoader, Preloader} from '@components/Loader'
import PrivateRoute from '@components/PrivateRoute'
import {useTheme} from './ThemeProvider'
import '../styles/main.less'

const routes = [
  {
    isPublic: true,
    exact: true,
    path: '/login',
    component: lazy(() => import('@pages/Auth')),
  },
  {
    exact: true,
    path: '/',
    component: lazy(() => import('@pages/Home')),
  },
  {
    exact: true,
    path: '/marketing/:type',
    component: lazy(() => import('@pages/Marketing')),
  },
  {
    exact: true,
    path: '/admin/:type?/:action?/:userId?',
    component: lazy(() => import('@pages/Admin')),
  },
  {
    exact: true,
    path: '/profile',
    component: lazy(() => import('@pages/Profile')),
  },
  {
    exact: true,
    path: '/leaderboard/:type?',
    component: lazy(() => import('@pages/Leaderboard')),
  },
  {
    exact: true,
    path: '/finance/:type?',
    component: lazy(() => import('@pages/Finance')),
  },
  {
    isPublic: true,
    path: '*',
    component: lazy(() => import('@pages/NotFound')),
  },
]

const AppContainer = () => {
  const theme = useTheme()
  const load = useSelector((state) => state.system.load)

  const routeComponents = useMemo(
    () =>
      routes.map(({isPublic, ...route}) => (
        <PrivateRoute key={route.path} isPublic={isPublic} {...route} />
      )),
    [],
  )

  return (
    <>
      <Preloader loaded={theme.applied && !load} themeChanged={!load} />
      <MainLayout showHeader={!load}>
        <Suspense fallback={<PageLoader loaded={!theme.applied && !load} />}>
          <Switch>{routeComponents}</Switch>
        </Suspense>
      </MainLayout>
    </>
  )
}

export default AppContainer


import React, { lazy } from "react";
import { Layout } from "antd";
import { Switch } from "react-router-dom";
import PrivateRoute from "@components/PrivateRoute";

const routes = [
    {
        exact: true,
        path: "/marketing/CRM",
        component: lazy( () => import("./pages/CRM") )
    },
    {
        exact: false,
        path: "/marketing/pivot",
        component: lazy( () => import("./pages/Pivot") )
    },
    {
        exact: true,
        path: "/marketing/affiliates",
        component: lazy( () => import("./pages/Affiliates") )
    }
];

const Marketing = () => {
    return (
        <Layout.Content>
            <Switch>
                {routes.map( ( route ) => (
                    <PrivateRoute key={route.path} {...route} />
                ) )}
            </Switch>
        </Layout.Content>
    );
};

export default Marketing;