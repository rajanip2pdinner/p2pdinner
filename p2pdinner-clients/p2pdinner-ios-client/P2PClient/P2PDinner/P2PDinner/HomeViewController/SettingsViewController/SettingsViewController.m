//
//  SettingsViewController.m
//  P2PDinner
//
//  Created by Selvam M on 5/31/16.
//  Copyright © 2016 P2PDinner. All rights reserved.
//

#import "SettingsViewController.h"
#import "AgreementsViewController.h"
#import "StringConstants.h"
#import "AddFoodPhotos.h"

#import "LoginServiceHandler.h"
#import "SharedLogin.h"
@interface SettingsViewController ()

@end

@implementation SettingsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:kTitle_Settings];
    self.tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    NSString *profileId = [[NSUserDefaults standardUserDefaults] objectForKey:@"userId"];
    [UIApplication sharedApplication].networkActivityIndicatorVisible=YES;
    [[LoginServiceHandler sharedServiceHandler] getProfileDetails:profileId serviceCallBack:^(NSError *error, LoginResponce *response) {
        [UIApplication sharedApplication].networkActivityIndicatorVisible=NO;
        [self.tableView reloadData];
        
    }];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if ([[SharedLogin sharedLogin] userCertificates].length > 0 ) {
        return 4;
    }
    return 3;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.row == 3) {
        AddFoodPhotos *addFoodPhotos = [self.storyboard instantiateViewControllerWithIdentifier:@"addFoodPhotosViewController"];
        ItemDetails *itemDetails=[[ItemDetails alloc]init];
        itemDetails.imageUri = [[SharedLogin sharedLogin] userCertificates];
        [addFoodPhotos setTitle:kSafetyProfile];
        [addFoodPhotos setItemDetails:itemDetails];
        [addFoodPhotos setIsFromFoodSafty:YES];
        [addFoodPhotos setIsFromSettings:YES];
        [self.navigationController pushViewController:addFoodPhotos animated:YES];
        
        
//        [[LoginServiceHandler sharedServiceHandler] profileUpdateCertificate:@"https://www.filepicker.io/api/file/bISp3mAvQA6vWlTzaFoO" serviceCallBack:^(NSError *error, LoginResponce *response) {
//            if (!error) {
//                NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
//                [userDefaults setObject:@"https://www.filepicker.io/api/file/bISp3mAvQA6vWlTzaFoO" forKey:@"userCertificates"];
//                [userDefaults synchronize];
//            }
//            
//        }];
    }else{
      [self performSegueWithIdentifier:kSegueID_AgreementVC sender:indexPath];
    }

}
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    AgreementsViewController *viewController=[segue destinationViewController];
    NSString *filePath;
    NSIndexPath *indexPath=sender;
    if (indexPath.row==0) {
       filePath = kTerms_URL;
        [viewController setTitle:kTermsConditionText];
    }
    else if (indexPath.row==1){
        filePath = kCopyright_URL;
        [viewController setTitle:kCopyrightText];
    }else if (indexPath.row==2){
        filePath = kPrivacy_URL;
        [viewController setTitle:kPrivacyText];
    }
     NSURL *targetURL = [NSURL URLWithString:filePath];
     [viewController setPdfURL:targetURL];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
